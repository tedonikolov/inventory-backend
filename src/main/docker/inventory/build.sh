docker stop $(docker ps -a -q)
cd ../../../../../inventory-backend
chmod +x ./gradlew
./gradlew clean assemble
cd src/main/docker/inventory
docker compose up -d --build