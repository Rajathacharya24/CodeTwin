# Architecture Decisions

## Why Modular Monolith?
A modular monolith provides the simplicity of a single deployment unit while maintaining the clear boundaries and strict decoupling found in microservices. It's the ideal starting point for a scalable architecture, allowing us to extract individual modules into separate services only if and when required by the product's scale.

## Why Java 21?
Java 21 is a Long-Term Support (LTS) release offering robust features like Virtual Threads (Project Loom), which will be extremely beneficial for parallelizing operations such as source-code analysis, graph traversal, and AI API calls without heavy thread pool overhead.

## Why PostgreSQL?
PostgreSQL is a robust, ACID-compliant relational database. It is sufficient for the initial domain entities. Crucially, it supports `pgvector`, which will be essential when we introduce vector embeddings for AI similarity searches and code retrieval without needing a separate database like Neo4j or Elasticsearch initially.

## Why AI Provider Abstraction?
Different AI models excel at different tasks (e.g., Anthropic for large context reasoning, OpenAI for specific coding tasks, local Ollama for privacy-sensitive data). Decoupling the business logic from a specific provider ensures flexibility and resilience against vendor lock-in or API outages.

## Why is Code Analysis separated from Architecture Analysis?
Code analysis deals with the raw extraction of syntax, dependencies, and file structures. Architecture analysis uses this raw data to infer higher-level concepts (components, layers, health, debt). Separating them ensures the architecture model isn't tightly coupled to the specifics of the JavaParser or AST representations.

## Why is Impact Analysis a separate module?
Impact analysis aggregates data from architecture, git, and code analysis to calculate risks and recommendations. Keeping it separate prevents circular dependencies and maintains clear boundaries for this specific, complex business capability.

## Future Expansion Strategy
- **JavaParser**: Will be implemented in the `infrastructure/parser` layer of the `analysis` module.
- **Git**: Will be implemented in the `infrastructure/external` layer of the `git` module, abstracting local `.git` and remote (GitHub/GitLab) operations.
- **AI Providers**: Implementations for OpenAI, Anthropic, etc., will reside in `infrastructure/external` within the `ai` module, implementing a common interface from the `ai/domain` layer.
- **pgvector**: Will be integrated into PostgreSQL to store embeddings of code snippets and architectural decisions.
- **Graph analysis / Neo4j**: May be introduced into the `architecture` module if relational recursion in Postgres becomes a bottleneck for deep dependency traversal.
- **Security / Telemetry**: Additional modules can be added horizontally without impacting existing core modules.
