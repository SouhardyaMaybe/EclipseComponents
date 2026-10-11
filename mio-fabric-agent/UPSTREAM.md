# Provenance and design: mio-fabric-agent

## What the original jar was

`MioFabricAgent.jar` (57,435 B) shipped as an opaque prebuilt asset in the
launcher app (`EclipseLauncher/src/main/assets/components/components/`) and in
every PojavLauncher/ZalithLauncher-lineage launcher we surveyed (e.g.
ZalithLauncher ships the same file name and the same
`LibPath.MIO_FABRIC_AGENT` constant). It contained three classes of its own
(`com.mio.PreMainAgent`, `com.mio.MyTransformer` and two anonymous visitors)
plus a bundled copy of ASM 5.0.1.

- Upstream source: NONE PUBLICLY AVAILABLE. Searched 2026-10-11 via GitHub
  code/repo search, `gh search code` for the transformed class name
  (`net/fabricmc/installer/client/ClientInstaller`), author repo listings
  (ShirosakiMio, who published the sibling `MioLibPatcher` component), and
  the ZalithLauncher/FCL/PojavLauncher trees. Every launcher only redistributes
  the binary. The `com.mio` package prefix suggests the same author as
  MioLibPatcher (https://github.com/ShirosakiMio/MioLibPatcher), but this
  could not be confirmed and no license exists anywhere for this code.
- License of the original: NONE FOUND (no license file, no headers). The
  upstream author retains all rights; this module therefore contains no
  upstream code at all - it is an independent reimplementation of the
  observed runtime behavior, written from scratch and released under this
  repository's MIT license (see the root `LICENSE` and `NOTICE.md`).

## Behavior of the original

Established via `javap -p -c` on the shipped classes (findings recorded in
`/root/new/rewrite-verification/P3-COMPONENTS-RELEASE.md`):

- `PreMainAgent.premain(String, Instrumentation)` registered
  `MyTransformer` with `addTransformer(transformer, true)` (retransformation
  enabled) and printed a status line to stdout.
- `PreMainAgent.agentmain(...)` did the same, then scanned
  `getAllLoadedClasses()` for a class whose `getName()` equals the *simple*
  name `"ClientInstaller"` and retransformed it. (Quirk kept verbatim: the
  check compares against the simple name while loaded classes report
  `net.fabricmc.installer.client.ClientInstaller`, so this attach-only path
  never matched; the launcher only ever uses `-javaagent`, i.e. `premain`.)
- `MyTransformer.transform(...)` matched the internal name
  `net/fabricmc/installer/client/ClientInstaller`, then ran an ASM visitor
  chain over it: for every method named `install`, after each `astore` into
  local variable slot 4 it injected `ldc "profileName";
  invokestatic java/lang/System.getProperty`, followed by a repeated
  `astore 4`. Net effect: whenever the installer stores a value into local 4
  (the profile-name local of `ClientInstaller.install`), the value is
  immediately overwritten with the `profileName` system property the launcher
  passes as `-DprofileName="<custom name>"`. Any transformation failure was
  caught (`Throwable`), printed, and the original bytes were returned.

## What the rewrite does

`me.shadow.eclipse.miofabricagent` reproduces exactly that behavior with our
own code:

- Same manifest contract: `Premain-Class`/`Agent-Class` now point at
  `me.shadow.eclipse.miofabricagent.MioFabricAgent`, plus
  `Can-Redefine-Classes: true` and `Can-Retransform-Classes: true`.
- Same injection heuristic (same target class, same method-name filter, same
  `astore` slot 4 hook, same `System.getProperty("profileName")` payload).
- Same failure mode (catch `Throwable`, print, keep the original class bytes).
- Deliberate differences, both strictly behavior-preserving in the launcher's
  use: `transform` returns `null` (instead of the unchanged byte array) for
  non-matching classes, and the rewritten visitor reads the class bytes
  handed to `transform` instead of re-fetching them through the system
  class loader. Bundled ASM is 9.7.1 (BSD, from Maven Central) instead of the
  original's ASM 5.0.1; the visitor chain uses the ASM9 API level, a
  superset of what the transform touches.

## Runtime contract

- Launched by the app during Fabric profile installation:
  `-DprofileName="<name>" -javaagent:<data>/components/MioFabricAgent.jar
  -jar <fabric-installer.jar> client -mcversion ... -loader ... -dir ...`
  (see the app's `InstallArgsUtils.setFabric`). The `-DprofileName` property
  must be set; the agent reads it but never validates it (same as the
  original).
- The agent's own classes plus shaded ASM are fully self-contained in the
  jar. The transformed target class (`net.fabricmc.installer.client.ClientInstaller`)
  is provided by the Fabric installer jar on the install-time classpath; the
  agent never links against Fabric/Minecraft classes itself.
- Bytecode level 52 (Java 8), matching the original classes: the install JVM
  is the launcher's JRE 8 or JRE 17 runtime, both of which accept v52.
