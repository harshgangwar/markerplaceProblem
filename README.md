# Marketplace CLI - Java 8

Build:
```bash
./build.sh
```
Run:
```bash
./run.sh < input.txt
```

Architecture:
- model: domain entities and enums
- repository: persistence abstraction + thread-safe in-memory implementations
- service: business rules and orchestration
- cli: parsing and command dispatch
- app: composition root
- util: parsing and formatting helpers

No external libraries are used. Java 8 compatible.
