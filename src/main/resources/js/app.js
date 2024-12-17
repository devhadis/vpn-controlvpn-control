function connectVPN() {
    const regionId = document.getElementById('region').value;

    fetch(`/vpn/start/${regionId}`, {
        method: 'POST'
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('output').innerText = `VPN Started: ${data}`;
    })
    .catch(error => {
        document.getElementById('output').innerText = `Error: ${error}`;
    });
}

function stopVPN() {
    fetch(`/vpn/stop`, {
        method: 'POST'
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('output').innerText = `VPN Stopped: ${data}`;
    })
    .catch(error => {
        document.getElementById('output').innerText = `Error: ${error}`;
    });
}
