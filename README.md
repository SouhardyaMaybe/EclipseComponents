# EclipseComponents

Build pipelines for the pure-Java components the Eclipse Launcher app ships in
its assets. This monorepo replaces the opaque prebuilt jars under
`EclipseLauncher/src/main/assets/components/**` with sources we build
ourselves, so every binary the launcher loads can be audited and rebuilt.

## Modules

| Module | Produces | Component | Status |
|---|---|---|---|
| `exp4j` | `exp4j-0.4.9-SNAPSHOT.jar` | math expression evaluator (`net.objecthunter:exp4j`) | vendored |
| `java-sandbox` | `pro-grade.jar` | pro-grade SecurityManager sandbox | vendored |
| `installer-agent` | `forge_installer.jar` | Forge/NeoForge installer agent (`git.artdeell.installer_agent`) | vendored |
| `optifine-renamer` | `OptiFineRenamer.jar` | OptiFine version renamer agent | vendored |
| `cacio-shared-8` | `cacio-shared-1.10-SNAPSHOT.jar` | caciocavallo CTC classes (JRE 8) | vendored |
| `cacio-androidnw-8` | `cacio-androidnw-1.10-SNAPSHOT.jar` | caciocavallo Android window backend (JRE 8) | vendored |
| `cacio-shared-17` | `cacio-shared-1.19.1-SNAPSHOT.jar` | caciocavallo CTC classes (JRE 17+) | vendored |
| `cacio-tta-17` | `cacio-tta-1.19.1-SNAPSHOT.jar` | caciocavallo Swing/TTA layer (JRE 17+) | vendored |
| `cacio-agent-17` | `cacio-agent.jar` | caciocavallo java agent (JRE 17+) | vendored |
| `mio-lib-patcher` | `MioLibPatcher.jar` | MioLibPatcher bytecode transform agent | vendored (no upstream license - see `UPSTREAM.md`) |
| - | `ResConfHack.jar` | AWT resource-configuration hack | TODO rewrite (no public source found) |
| - | `MioFabricAgent.jar` | Fabric launch agent | TODO rewrite (no public source found) |

Per-module provenance (upstream URL, commit, license) is recorded in each
module's `UPSTREAM.md`; the full research table lives in `NOTICE.md`.

All upstream trees are vendored as plain source directories - upstream `.git`
history is intentionally not preserved. Each module keeps the upstream license
text in a `LICENSE` file.

## Consumption model

The launcher app does not build this repo. It consumes pinned artifact URLs:
each component version bump in the app corresponds to a tagged build of this
repo whose jars are attached to the GitHub release for that tag. The jar
file names above match the file names already shipped in
`EclipseLauncher/src/main/assets/components/**`, so a pinned URL swap is a
drop-in replacement.

## Building

Requirements: JDK 17 (runs Gradle; module toolchains for JDK 8/17 are
auto-provisioned via the foojay resolver), Gradle 8.x.

```sh
gradle build                 # all modules
gradle :exp4j:jar            # one module
```

Jar outputs land in `<module>/build/libs/`.

CI (`.github/workflows/build.yml`) builds every module on each push, uploads
each jar as a workflow artifact, and attaches jars to a GitHub release when a
tag is pushed.

DRAFT notes for the first CI iterations:

- Only main sources are compiled by CI (`gradle :module:jar`); upstream test
  trees are vendored for reference but not yet executed.
- The `cacio-*` modules compile against JDK internals; the `--add-exports`
  flag lists in their build files are expected to need extension on the first
  runs.
- The fat-jar and manifest settings were reconstructed from upstream build
  files; verify agent manifests after the first green run.

## Adding / rewriting a component

1. Vendor the upstream source tree under `src/main/java` (no `.git` history).
2. Add `UPSTREAM.md` (URL, commit, license) and the upstream `LICENSE` file.
3. Add a `java-library` module with `archivesName` matching the launcher's
   expected jar name.
4. Add the module to `settings.gradle.kts` and to the CI matrix.
