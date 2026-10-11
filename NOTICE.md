# Component inventory

All binaries the Eclipse Launcher app used to ship as opaque prebuilt files.

| Launcher artifact (assets) | Module here | Upstream source | Upstream license | Status |
|---|---|---|---|---|
| `exp4j-0.4.9-SNAPSHOT.jar` | `exp4j` | https://github.com/fasseg/exp4j (archived; PojavLauncherTeam/exp4j is the downstream fork) | Apache-2.0 | vendored |
| `pro-grade.jar` | `java-sandbox` | https://github.com/pro-grade/pro-grade | Apache-2.0 | vendored |
| `forge_installer.jar` | `installer-agent` | https://github.com/PojavLauncherTeam/PojavLauncher `forge_installer/` (package `git.artdeell.installer_agent`) | LGPL-3.0 | vendored |
| `OptiFineRenamer.jar` | `optifine-renamer` | https://github.com/ZalithLauncher/OptiFineRenamer | MIT | vendored |
| `cacio-shared-1.10-SNAPSHOT.jar` | `cacio-shared-8` | https://github.com/PojavLauncherTeam/caciocavallo `cacio-shared/` | GPL-2.0 | vendored |
| `cacio-androidnw-1.10-SNAPSHOT.jar` | `cacio-androidnw-8` | https://github.com/PojavLauncherTeam/caciocavallo `cacio-androidnw/` | GPL-2.0 | vendored |
| `ResConfHack.jar` | `resconf-hack` | no public source for the blob; class derived from OpenJDK 8 `sun/net/dns/ResolverConfigurationImpl.java` (https://github.com/openjdk/jdk8u) with the `ext.net.resolvPath` behavior observed in the PojavLauncher-lineage prebuilt | GPL-2.0 WITH Classpath exception (see module `LICENSE`) | own rewrite |
| `cacio-shared-1.19.1-SNAPSHOT.jar` | `cacio-shared-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-shared/` | GPL-2.0 | vendored |
| `cacio-tta-1.19.1-SNAPSHOT.jar` | `cacio-tta-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-tta/` | GPL-2.0 | vendored |
| `cacio-agent.jar` | `cacio-agent-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-agent/` | GPL-2.0 | vendored |
| `MioLibPatcher.jar` | `mio-lib-patcher` | https://github.com/ShirosakiMio/MioLibPatcher | NONE (no license file upstream) | vendored, license caveat |
| `MioFabricAgent.jar` | `mio-fabric-agent` | NONE FOUND - prebuilt blob propagated through the PojavLauncher/ZalithLauncher-lineage launchers (`com.mio` package, no repository publishes its source); rewritten from decompiled behavior | NONE for the blob; the rewrite is original code under this repo's MIT license | own rewrite |

Research performed 2026-10-09 via GitHub search/API, and extended 2026-10-11
for the two rewritten components. "NOT FOUND" means repeated searches (repo
search, code search, author repo listings) turned up no public source; for
those two components the functionality has been reimplemented in this repo
(`mio-fabric-agent` from decompiled behavior, `resconf-hack` as a fresh
transcription of the OpenJDK 8 class plus the observed behavioral change).

Licensing summary of vendored trees:

- Apache-2.0: `exp4j`, `java-sandbox`
- MIT: `optifine-renamer`
- GPL-2.0: all `cacio-*` modules (each keeps the upstream GPL text; artifacts
  built from them are distributed under GPL-2.0)
- GPL-2.0 WITH Classpath exception: `resconf-hack` (derived from the OpenJDK
  8 class of the same name; see its `UPSTREAM.md`)
- LGPL-3.0: `installer-agent`
- No license upstream: `mio-lib-patcher` (see its `UPSTREAM.md` before
  redistributing)
- MIT (this repo's own code): `mio-fabric-agent` (independent rewrite of an
  unlicensed blob; no upstream code is included)

The original scaffolding in this repository (build files, workflow, docs) is
MIT-licensed (see `LICENSE`).
