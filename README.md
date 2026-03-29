# HandShaker-Core

Standalone core library extracted from HandShaker `common` module.

## What this repo contains

- Cross-loader handshake validation logic
- Shared config and command engine parts
- Shared APIs for HandShaker module integrations

## Publishing

GitHub Actions publishes this library to GitHub Packages on each push to `main`.
No `.env` file is required in CI.

Coordinates:

- Group: `me.mklv`
- Artifact: `handshaker-core`
- Version: from `core_version` in `gradle.properties`

Repository URL format:

- `https://maven.pkg.github.com/<owner>/<repo>`

## Automatic version bump

Workflow behavior:

1. Publish current `core_version`
2. Commit next patch version back to `main`

Example:

- `core_version=1.0.0` in repo
- Next push publishes `1.0.0`
- Workflow commits `core_version=1.0.1`
- Next push publishes `1.0.1`

To bump major/minor manually, edit `core_version` before pushing.
