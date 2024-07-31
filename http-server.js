const { exec } = require('child_process');
const path = require('path');

const port = 8000;
const directory = path.join(__dirname, 'allure-report');

const command = `http-server ${directory} -p ${port}`;

exec(command, (error, stdout, stderr) => {
    if (error) {
        console.error(`Error: ${error.message}`);
        return;
    }
    if (stderr) {
        console.error(`Stderr: ${stderr}`);
        return;
    }
    console.log(`Stdout: ${stdout}`);
});
