# Upstream provenance: java-sandbox (pro-grade)

- Upstream: https://github.com/pro-grade/pro-grade
- Branch: master
- Vendored commit: 371b0d1349c37bd048a06206d3fa1b0c3c1cd9ab (2017-12-16)
- License: Apache-2.0 (full text in `LICENSE.txt`)
- Notes: Java SecurityManager sandbox used by the launcher's `java_sandbox.policy`
  flow. Only `src/main` was copied; `src/test` and `src/site` were dropped from
  the build (test sources are not compiled or executed by CI).
