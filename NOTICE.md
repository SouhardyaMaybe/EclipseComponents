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
| `ResConfHack.jar` | (none) | NOT FOUND - no public source located; only prebuilt jars are referenced across launchers | unknown | TODO rewrite |
| `cacio-shared-1.19.1-SNAPSHOT.jar` | `cacio-shared-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-shared/` | GPL-2.0 | vendored |
| `cacio-tta-1.19.1-SNAPSHOT.jar` | `cacio-tta-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-tta/` | GPL-2.0 | vendored |
| `cacio-agent.jar` | `cacio-agent-17` | https://github.com/FCL-Team/caciocavallo17 `cacio-agent/` | GPL-2.0 | vendored |
| `MioLibPatcher.jar` | `mio-lib-patcher` | https://github.com/ShirosakiMio/MioLibPatcher | NONE (no license file upstream) | vendored, license caveat |
| `MioFabricAgent.jar` | (none) | NOT FOUND - referenced by FCL/ZalithLauncher-lineage launchers but no public repository exists | unknown | TODO rewrite |

Research performed 2026-10-09 via GitHub search/API. "NOT FOUND" means repeated
searches (repo search, code search, author repo listings) turned up no public
source; for those two components the plan is to reimplement the needed
functionality in this repo (clean-room) or to obtain source/permission from the
original authors.

Licensing summary of vendored trees:

- Apache-2.0: `exp4j`, `java-sandbox`
- MIT: `optifine-renamer`
- GPL-2.0: all `cacio-*` modules (each keeps the upstream GPL text; artifacts
  built from them are distributed under GPL-2.0)
- LGPL-3.0: `installer-agent`
- No license upstream: `mio-lib-patcher` (see its `UPSTREAM.md` before
  redistributing)

The original scaffolding in this repository (build files, workflow, docs) is
MIT-licensed (see `LICENSE`).
