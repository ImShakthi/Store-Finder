Create an application that shows the 5 closest Jumbo stores to a given position.

You can find a list of Jumbo stores in JSON format attached to this document.

There are just a few rules:

- It has to be Java programming language. What frameworks, libraries you use is up to you.
- We would like to see your skills to develop REST APIs
- Write your code as if it's production code as much as possible.
- Make sure the reviewer can easily run the application for evaluation purposes.

SELECT
id,
address_id,
city_id,
sap_store_id,
uuid,
ST_Distance(location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) AS distance_meters
FROM
store
WHERE
location IS NOT NULL
ORDER BY
location <-> ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)
LIMIT 5;


SELECT
id,
city_id,
ST_Distance(location, ST_SetSRID(ST_MakePoint(4.899431, 52.379189), 4326)) AS distance_meters
FROM
store
WHERE
location IS NOT NULL
ORDER BY
location <-> ST_SetSRID(ST_MakePoint(4.899431, 52.379189), 4326)
LIMIT 5;
