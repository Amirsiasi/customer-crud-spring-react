# Customer CRUD (Spring Boot + React + Vite)

## Run without Docker
### Backend
1. Open a terminal in `backend/`
2. Build & run: `./mvnw spring-boot:run` (or if you have Maven installed: `mvn spring-boot:run`)
   - App listens on http://localhost:8080
3. Optional: H2 Console at http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:customers`)

### Frontend
1. Open another terminal in `frontend/`
2. Install deps: `npm install`
3. Start dev server: `npm run dev`
   - Open the printed URL (usually http://localhost:5173)

## Run with Docker
1. In the project root run:
   ```bash
   docker compose build
   docker compose up
   ```
2. Open http://localhost:5173
   - Frontend is served by nginx (built files).
   - API base URL is set via `VITE_API_URL=http://localhost:8080` in docker-compose.

## API quick test
- `GET http://localhost:8080/api/customers`
- `POST http://localhost:8080/api/customers` with JSON:
  ```json
  { "firstName": "Jane", "lastName": "Doe", "email": "jane@doe.com" }
  ```

Enjoy!
