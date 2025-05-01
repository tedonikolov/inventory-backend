docker compose stop
cd ../../../../../inventory-backend
chmod +x ./gradlew
./gradlew clean assemble
cd src/main/docker/inventory
docker compose up -d --build