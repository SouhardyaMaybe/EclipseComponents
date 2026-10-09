# Upstream provenance: mio-lib-patcher

- Upstream: https://github.com/ShirosakiMio/MioLibPatcher
- Branch: main
- Vendored commit: e031d4cce2aa9274b106db51d286df2cefec6d40 (2026-10-03)
- License: NONE FOUND. The upstream repository contains no LICENSE file and no
  license statement in its READMEs (GitHub reports NOASSERTION). Absent an
  explicit license, the upstream author retains all rights. This copy is
  vendored only to track and rebuild the exact artifact the launcher ships;
  before redistributing this module's source or jar publicly, obtain permission
  from the author or reimplement the functionality.
- Notes: javassist-based bytecode transform agent (`com.mio.libpatcher`).
  DRAFT decision: built as a self-contained fat jar (javassist bundled);
  upstream ships the agent without bundling its dependency.
