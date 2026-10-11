package sun.net.dns;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Replacement for the JRE 8 implementation of {@link ResolverConfiguration}.
 *
 * The stock class reads {@code /etc/resolv.conf} directly, which does not
 * exist on Android. This replacement reads the resolv.conf-style file whose
 * path is given by the {@code ext.net.resolvPath} system property (the
 * launcher writes such a file and passes the property with every launch). If
 * the property is unset or the file cannot be read, a built-in fallback
 * configuration naming the 8.8.8.8 resolver is used instead, and the JVM's
 * DNS resolution keeps working in every case.
 *
 * The class is placed on the boot classpath ({@code -Xbootclasspath/p}) so it
 * shadows the copy inside rt.jar; the class and method names are therefore
 * fixed by the JRE contract and cannot be moved to another package. Logic is
 * derived from the OpenJDK 8 implementation of the same class (GPL-2.0 with
 * Classpath exception, see UPSTREAM.md and LICENSE).
 */
public class ResolverConfigurationImpl extends ResolverConfiguration {

    // Lock held whilst loading configuration or checking.
    private static final Object lock = new Object();

    // Time of last refresh.
    private static long lastRefresh = -1;

    // Cache timeout (300 seconds).
    private static final int TIMEOUT = 300000;

    // System property pointing at the resolv.conf-style file to parse.
    private static final String RESOLV_PATH_PROPERTY = "ext.net.resolvPath";

    // Content used when no resolv.conf file is available.
    private static final String FALLBACK_RESOLV_FILE = "nameserver 8.8.8.8";

    private static final String FALLBACK_MESSAGE_NOT_SET =
            "ResConfHack/E: Resolv file path not set, 8.8.8.8 nameserver used by default";

    // Resolver options.
    private final Options opts;

    private LinkedList<String> searchlist;
    private LinkedList<String> nameservers;

    ResolverConfigurationImpl() {
        opts = new OptionsImpl();
    }

    /**
     * Opens the resolv.conf-style reader: the file named by the
     * {@code ext.net.resolvPath} system property, or the built-in fallback
     * configuration when the property is missing or the file is unreadable.
     */
    private static BufferedReader openResolvFile() throws IOException {
        String resolvPath = System.getProperty(RESOLV_PATH_PROPERTY);
        if (resolvPath == null) {
            System.out.println(FALLBACK_MESSAGE_NOT_SET);
            return new BufferedReader(new StringReader(FALLBACK_RESOLV_FILE));
        }
        try {
            return new BufferedReader(new FileReader(resolvPath));
        } catch (IOException e) {
            System.out.println("ResConfHack/E: Unable to read resolv.conf: " + e.getMessage());
            return new BufferedReader(new StringReader(FALLBACK_RESOLV_FILE));
        }
    }

    /**
     * Parses the resolv.conf-style file for the values of a particular
     * keyword: at most {@code maxperkeyword} values per matching line and at
     * most {@code maxkeywords} matching lines.
     */
    private LinkedList<String> resolvconf(String keyword, int maxperkeyword, int maxkeywords) {
        LinkedList<String> values = new LinkedList<>();
        try {
            BufferedReader in = openResolvFile();
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    int maxvalues = maxperkeyword;
                    if (line.length() == 0) {
                        continue;
                    }
                    if (line.charAt(0) == '#' || line.charAt(0) == ';') {
                        continue;
                    }
                    if (!line.startsWith(keyword)) {
                        continue;
                    }
                    String value = line.substring(keyword.length());
                    if (value.length() == 0) {
                        continue;
                    }
                    if (value.charAt(0) != ' ' && value.charAt(0) != '\t') {
                        continue;
                    }
                    StringTokenizer st = new StringTokenizer(value, " \t");
                    while (st.hasMoreTokens()) {
                        String val = st.nextToken();
                        if (val.charAt(0) == '#' || val.charAt(0) == ';') {
                            break;
                        }
                        if ("nameserver".equals(keyword)) {
                            if (val.indexOf(':') >= 0 &&
                                    val.indexOf('.') < 0 && // skip IPv4 literals with port
                                    val.indexOf('[') < 0 &&
                                    val.indexOf(']') < 0) {
                                // IPv6 literal, in non-BSD-style.
                                val = "[" + val + "]";
                            }
                        }
                        values.add(val);
                        if (--maxvalues == 0) {
                            break;
                        }
                    }
                    if (--maxkeywords == 0) {
                        break;
                    }
                }
            } finally {
                in.close();
            }
        } catch (IOException ioe) {
            // Problem reading the file; keep whatever was parsed so far.
        }
        return values;
    }

    // Load DNS configuration from the resolv.conf-style file.
    private void loadConfig() {
        assert Thread.holdsLock(lock);

        // Check if cached settings have expired.
        if (lastRefresh >= 0) {
            long currTime = System.currentTimeMillis();
            if ((currTime - lastRefresh) < TIMEOUT) {
                return;
            }
        }

        // Get the name servers.
        nameservers =
                java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction<LinkedList<String>>() {
                            public LinkedList<String> run() {
                                // typically MAXNS is 3 but we've picked 5 here
                                // to allow for additional servers if required.
                                return resolvconf("nameserver", 1, 5);
                            } /* run */
                        });

        // Get the search list (or domain).
        searchlist = getSearchList();

        // Update the timestamp on the configuration.
        lastRefresh = System.currentTimeMillis();
    }

    // Obtain search list or local domain.
    private LinkedList<String> getSearchList() {
        LinkedList<String> sl;

        // First try the search keyword in the resolv.conf-style file.
        sl = java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<LinkedList<String>>() {
                    public LinkedList<String> run() {
                        // First try search keyword (max 6 domains).
                        LinkedList<String> ll = resolvconf("search", 6, 1);
                        if (ll.size() > 0) {
                            return ll;
                        }
                        return null;
                    } /* run */
                });
        if (sl != null) {
            return sl;
        }

        // No search keyword so use local domain
        // (LOCALDOMAIN has absolute priority on Solaris).
        String localDomain = localDomain0();
        if (localDomain != null && localDomain.length() > 0) {
            sl = new LinkedList<String>();
            sl.add(localDomain);
            return sl;
        }

        // Try domain keyword in the resolv.conf-style file.
        sl = java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<LinkedList<String>>() {
                    public LinkedList<String> run() {
                        LinkedList<String> ll = resolvconf("domain", 1, 1);
                        if (ll.size() > 0) {
                            return ll;
                        }
                        return null;
                    } /* run */
                });
        if (sl != null) {
            return sl;
        }

        // No local domain so try fallback (RPC) domain or hostName.
        sl = new LinkedList<>();
        String domain = fallbackDomain0();
        if (domain != null && domain.length() > 0) {
            sl.add(domain);
        }
        return sl;
    }

    // ----

    @SuppressWarnings("unchecked")
    public List<String> searchlist() {
        synchronized (lock) {
            loadConfig();
            // List is mutable so return a shallow copy.
            return (List<String>) searchlist.clone();
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> nameservers() {
        synchronized (lock) {
            loadConfig();
            // List is mutable so return a shallow copy.
            return (List<String>) nameservers.clone();
        }
    }

    public Options options() {
        return opts;
    }

    // --- Native methods (provided by libnet, loaded below) --

    static native String localDomain0();

    static native String fallbackDomain0();

    static {
        java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction<Void>() {
                    public Void run() {
                        System.loadLibrary("net");
                        return null;
                    }
                });
    }
}

/**
 * Implementation of {@link ResolverConfiguration.Options}.
 */
class OptionsImpl extends ResolverConfiguration.Options {
}
