const
    readline = require('readline')

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
}),
    fs = require("node:fs")

fs.readFile("stringOfNames.txt", "utf8", (err, stringOfNames) =>
    processPOJOs(stringOfNames.trim().split(/\s+/)))

function processPOJOs(variableNames = [], i = 0, gottenPOJOs = []){

    const variableName = variableNames[i]
    rl.question(`${variableName}: `, variableType => {
        gottenPOJOs.push(`private ${variableType}\t\t${variableName};`)
        i += 1
        if(i < variableNames.length)
            processPOJOs(variableNames, i, gottenPOJOs)
        else {
            console.log(`${gottenPOJOs.length} получено:\n${gottenPOJOs.join("\n")}`)
            rl.close();
        }
    });
}

