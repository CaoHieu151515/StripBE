echo "Start pulling latest source code from remote..."
git fetch origin develop
git pull origin develop
echo "Pulling latest source code from remote successfully..."

echo "Start packaging and containerizing the application..."
npm install
docker-compose up --build -d
echo "Packaging and containerizing the application successfully..."

echo "Start remove dangling images..."
docker rmi $(docker images -f "dangling=true" -q)
echo "Remove dangling images successfully..."
