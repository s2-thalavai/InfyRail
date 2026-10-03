# InfyRail – Spring REST

Base URL: `http://localhost:8765/InfyRail`  (Spring Boot 3.3, Java 17, MySQL schema `infyrail`)

## Run
1. Update `spring.datasource.username/password` in `application.properties`.
2. `mvn spring-boot:run` – tables are created by Hibernate and seeded from `data.sql`
   (or run `infyrail-db.sql` manually first).
3. Import `InfyRail.postman_collection.json` into Postman.

## Endpoints

| # | Operation | Method & URL | Body | Response |
|---|-----------|--------------|------|----------|
| 1 | Create route | `POST /routes` | RouteDTO JSON | `201` route id (e.g. `104`) |
| 2 | View route | `GET /routes/101` | – | `200` RouteDTO |
| 3 | Trains by source/destination | `GET /routes/trains?source=Bangalore&destination=Chennai` | – | `200` List<RouteDTO> |
| 4 | Update route (matrix params) | `PUT /routes/101;source=Bengaluru;destination=Chennai` | none | `200` RouteDTO |
| 5 | Delete train from route | `DELETE /routes/101/16021` | – | `200` message |
| 6 | Add/update train in route | `PUT /routes/101` | TrainDTO JSON | `200` message |
| 7 | Create train | `POST /trains` | TrainDTO JSON | `201` train id |
| 8 | Update fare | `PUT /trains/12608` | `1050.75` (raw JSON) | `200` message |

#4 and #6 share the same URL and method: a request **without** a JSON body goes to #4,
a request **with** `Content-Type: application/json` goes to #6.

### Sample bodies
Create route (train with `id` = link/update an existing train, no `id` = create a new one):
```json
{
  "source": "Chennai",
  "destination": "Coimbatore",
  "trainList": [
    { "trainName": "Kovai Express", "arrivalTime": "06:00:00", "departureTime": "06:10:00", "fare": 650.50 }
  ]
}
```
Train (for #6 and #7):
```json
{ "id": 12608, "trainName": "Lalbagh SF Express", "arrivalTime": "18:30:00", "departureTime": "18:45:00", "fare": 999.99 }
```

## Negative tests
| Request | Expected message |
|---------|------------------|
| `GET /routes/99` or `/routes/1234` | Route id should be a 3 digit number |
| `GET /routes/abc` | Invalid request ... |
| `GET /routes/555` | ROUTEID_NOT_FOUND ... |
| `GET /routes/trains?source=Bang1&destination=Chennai` | Source should contain only alphabets |
| `POST /routes` with `"source": ""`, `"trainList": []` | Source should not be empty, ..., Train list should not be empty |
| `DELETE /routes/101/12910` | The given train is not part of this route |
| `PUT /trains/12608` body `-5` | Fare should be greater than zero |

Error response shape:
```json
{ "errorCode": 400, "message": "Route id should be a 3 digit number", "timestamp": "2026-10-03T10:15:30" }
```
