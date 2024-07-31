// server.js
const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 8000;

const requestHandler = (req, res) => {
    console.log(req.url);
    if (req.url === '/') {
        fs.readFile(path.join(__dirname, 'index.html'), 'utf8', (err, data) => {
            if (err) {
                res.writeHead(500);
                res.end('Error loading index.html');
                return;
            }
            res.writeHead(200, { 'Content-Type': 'text/html' });
            res.end(data);
        });
    } else if (req.url === '/script.js') {
        fs.readFile(path.join(__dirname, 'script.js'), 'utf8', (err, data) => {
            if (err) {
                res.writeHead(500);
                res.end('Error loading script.js');
                return;
            }
            res.writeHead(200, { 'Content-Type': 'application/javascript' });
            res.end(data);
        });
    } else {
        res.writeHead(404);
        res.end('Not Found');
    }
};

const server = http.createServer(requestHandler);

server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
