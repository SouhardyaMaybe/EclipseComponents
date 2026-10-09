# Upstream provenance: installer-agent (forge installer agent)

- Upstream: https://github.com/PojavLauncherTeam/PojavLauncher
- Path: `forge_installer/` (package `git.artdeell.installer_agent`)
- Branch: v3_openjdk
- Vendored commit: b12ad048157b3aa255d078c235dd4571e1900309 (2025-09-23)
- License: LGPL-3.0 (full text in `LICENSE`, inherited from the PojavLauncher repo)
- Notes: the launcher runs this agent under the bundled JRE to execute
  Forge/NeoForge installer jars with AWT filtering (headless-ish GUI).
  Only the `forge_installer/src/main/java` tree was vendored.
