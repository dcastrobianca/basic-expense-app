## App spec

A tiny expense service. It practices operations, not business logic.

Behavior:

- POST /expenses takes a description, an amount and a currency (EUR, GBP, USD or BRL). It asks Frankfurter for the current rate, converts the amount to EUR, and stores everything in PostgreSQL.
- GET /expenses lists the stored rows.
- GET /actuator/health is open and reports the database state.
- Both /expenses endpoints require an X-API-Key header and return 401 without it.

Operations:

- Config file with defaults, overridable by environment variables: server port (8080), database URL, user and password, API key, external API base URL, connect and read timeouts.
- No secret in the code, the repo or the logs.
- External call timeouts: 3 s connect, 5 s read. If the service is unreachable, the endpoint returns 502 with a short message and the app keeps running.
- Schema created by a migration (Flyway).
- One log line per request to standard output.
- Maven build, one runnable jar, using the Java LTS version available from the VM's apt repositories (check first).