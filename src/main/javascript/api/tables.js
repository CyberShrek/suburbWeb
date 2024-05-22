
export function retrieveMainTable() {
    return retrieveTable("main")
}

export function retrieveAdiTable(idnum) {
    return retrieveTable("adi", idnum)
}

export function retrieveCostTable(idnum) {
    return retrieveTable("cost", idnum)
}

function retrieveTable(name, idnum) {
    return fetch(`/suburb/table/${name}${idnum? '/' + idnum : ''}`)
        .then(response => response.json())
        .then(result => Array.isArray(result) ? result : [result])
        .catch(error => alert(error.message))
}