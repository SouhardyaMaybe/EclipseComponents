# Provenance and design: resconf-hack

## What the original jar was

`ResConfHack.jar` (~12 KB) shipped as an opaque prebuilt asset in the launcher
app (`EclipseLauncher/src/main/assets/components/caciocavallo/`) and in every
PojavLauncher/ZalithLauncher/FCL-lineage launcher we surveyed. It contained
`sun.net.dns.ResolverConfigurationImpl` plus its inner classes and
`sun.net.dns.OptionsImpl` - i.e. a modified copy of the JRE 8 class of the
same name - and nothing else.

- Public prebuilt provenance: the jar name and class layout match the file
  shipped since 2021 in PojavLauncher (`JavaApp/libs/caciocavallo/
  RedConfHack.jar` - see https://github.com/PojavLauncherTeam/PojavLauncher
  and its iOS sibling) and redistributed by ZalithLauncher, FCL, HMCL-PE,
  QuestCraft/Pojlib and others. No repository publishes the source of the
  patch; only the binary circulates.
- The class itself is derived from OpenJDK 8
  (`jdk/src/solaris/classes/sun/net/dns/ResolverConfigurationImpl.java`,
  https://github.com/openjdk/jdk8u), GPL-2.0 with the Classpath exception
  (full text in `LICENSE`). Bytecode comparison (`javap -p -c`) against the
  stock OpenJDK 8 source shows the blob is the stock class with exactly one
  functional change: the hard-coded `/etc/resolv.conf` path in the
  `resolvconf` parser is replaced by the `ext.net.resolvPath` system property
  with a built-in `nameserver 8.8.8.8` fallback (see below). The stock class
  file even retained `private static final int TIMEOUT`, whose comment
  ("Cache timeout (300 seconds)") is quoted verbatim from OpenJDK 8.

## What the rewrite does

`resconf-hack` is a fresh transcription of the OpenJDK 8 class with the same
one-line behavioral change observed in the blob, written in this repository's
style (English comments, no Oracle headers; GPL-2.0+CE applies to the
derivative work - see `LICENSE`). Behavior is identical to the blob:

- `ext.net.resolvPath` set: parse that resolv.conf-style file.
- Property missing: print
  `ResConfHack/E: Resolv file path not set, 8.8.8.8 nameserver used by default`
  and parse the in-memory text `nameserver 8.8.8.8`.
- File unreadable (`IOException` while opening): print
  `ResConfHack/E: Unable to read resolv.conf: <message>` and use the same
  in-memory fallback.
- Everything else is stock OpenJDK 8 semantics: keyword line parsing
  (comments, `;`/`#` terminators, max 5 nameservers, IPv6 literals wrapped
  in brackets), the 300 s configuration cache, the `search` -> LOCALDOMAIN ->
  `domain` -> fallback-domain precedence, the two remaining native methods
  (`localDomain0`/`fallbackDomain0`, still backed by libnet, which the static
  initializer loads), and the package-private `OptionsImpl`.

## Deliberate differences (behavior-preserving)

- Diagnostic lines are identical in meaning; the log tag `ResConfHack/E:` is
  kept so on-device logs remain greppable for the component that ships under
  that jar name.
- The original blob's manifest declared `Main-Class: resconfhack.ResConfHack`,
  a class that does not exist inside the jar (`java -jar` on the original
  fails); the launcher never runs the jar as an application (it is only added
  to the boot classpath), so the rewrite emits a plain manifest without a
  dangling Main-Class.

## Runtime contract

- Java 8 only. The app adds every jar found in `<game home>/caciocavallo`
  to `-Xbootclasspath/p` when launching with a JRE 8 runtime
  (`LaunchArgs.getCacioJavaArgs`); shadowing `sun.net.dns.ResolverConfigurationImpl`
  requires the exact class name and package, so this module cannot use the
  repository's `me.shadow.eclipse.*` namespace - that is the JDK's own
  package and the whole point of the jar. On JRE 17 the app does not use this
  jar at all (the launcher's JRE 17 build is patched natively for
  `ext.net.resolvPath`).
- The app passes `-Dext.net.resolvPath=<data>/resolv.conf` with every launch
  and install (`JREUtils.getJavaArgs`), and unpacks a default `resolv.conf`
  asset next to it.
- Bytecode level 52 (Java 8), matching the original classes; the jar contains
  no dependencies and is entirely self-contained. The two native methods
  resolve against the JRE's own `libnet.so` exactly as in the stock class.

## Build note

The module compiles against the internal `sun.net.dns.ResolverConfiguration`
API, so its build file forks the toolchain's `javac` 8 binary with
`-XDignore.symbol.file=true` - the same approach the root build script uses
for the `cacio-*-8` modules.
