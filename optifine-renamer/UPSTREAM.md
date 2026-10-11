# Upstream provenance: optifine-renamer

- Upstream: https://github.com/ZalithLauncher/OptiFineRenamer
- Branch: main
- Vendored commit: acf8e665cd19fb9e8d3280c36781f45f3d6346d5 (2025-01-27)
- License: MIT (full text in `LICENSE.txt`)
- Notes: java agent (`com.movtery.optifine_renamer`) that renames OptiFine
  version internals for third-party launchers. Uses ASM 9.7.1 (declared in
  this module's build file). Agent manifest attributes are set by the Gradle
  jar task; the vendored `src/main/resources/META-INF/MANIFEST.MF` is kept for
  reference.
- Packaging: the `jar` task also unpacks the runtime dependency jars into the
  archive, so the released `OptiFineRenamer.jar` bundles `org.objectweb.asm`
  (ASM 9.7.1, BSD-3-Clause, https://asm.ow2.io) the way the app's original
  prebuilt blob does - the launcher starts the agent with a minimal classpath.
  ASM's Maven jar ships no license file inside the archive, so attribution
  lives here and in `NOTICE.md`; dependency `META-INF` signature/build
  metadata is excluded from the merge, and only this module's build file
  performs the merge.
