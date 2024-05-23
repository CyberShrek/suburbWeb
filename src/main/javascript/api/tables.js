
export function retrieveMainTable() {
    return retrieveTable("main")
}

export function retrieveAdiTable(idnum) {
    return retrieveTable("adi", idnum)
}

export function retrieveCostTable(idnum) {
    return retrieveTable("cost", idnum)
}

export function retrieveCO22(idnum) {
    return retrieveTransformed("co22", idnum)
}

function retrieveTable(name, idnum) {
    return retrieveJsonArray("/suburb/table/", name, idnum)
}

export function retrieveTransformed(name, idnum) {
    return retrieveJson("/suburb/transformation/", name, idnum)
        .then(json => json)
}

function retrieveJsonArray(path, name, idnum){
    return retrieveJson(path, name, idnum)
        .then(json => Array.isArray(json) ? json : [json])
}

function retrieveJson(path, name, idnum){
    return fetch(`${path}${name}${idnum? '/' + idnum : ''}`)
        .then(response => response.json())
        .catch(error => alert(error.message))
}