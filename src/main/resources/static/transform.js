new WebSocket("ws://" + location.host + "/suburb/logs").addEventListener("message", (event) => {
    const splitMessage = event.data.split("::")
    const log = {
        type:    splitMessage[0],
        message: splitMessage[1]
    }
    switch (log.type) {
        case "log"     : updateLog(log.message); break
        case "progress": updateProgress(log.message); break
        case "error"   : showError(log.message)
    }
})

const form = document.getElementById("form");
form.addEventListener("submit", async function (event) {
    event.preventDefault()
    form.submit.disabled = true
    await runTransformation()
    form.submit.disabled = false
})

function updateLog(message) {
    document.querySelector("#logs-container .list").innerHTML = message
}

function updateProgress(message) {
    console.log(message)
    document.querySelector("#logs-container progress")?.remove()
    if(Number(message) >= 0) {
        const progressElement = document.createElement("progress")
        progressElement.max = 100
        if (Number(message) > 0)
            progressElement.value = message

        document.querySelector("#logs-container").appendChild(progressElement)
    }
}

function showError(message) {
    document.querySelector("#logs-container progress")?.remove()
    alert(message)
}

async function runTransformation() {
    return await fetch("", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            date: form.date.value,
            prig: form.prig.checked,
            pass: form.pass.checked
        })
    }).catch(
        error => showError(error.message)
    )
}