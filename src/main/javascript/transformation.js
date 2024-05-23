import App from "./components/Transformation.svelte";

const addCSS = (name) => {
    return new Promise((resolve, reject) => {
        const link = document.createElement('link')
        link.rel = 'stylesheet'
        link.href = `/suburb/css/${name}.css`
        link.onload = resolve
        link.onerror = reject
        document.head.appendChild(link)
    })
}

Promise.all([
    addCSS("global"),
    addCSS("states"),
    addCSS("modal"),
    addCSS("table"),
    addCSS("loader"),
    addCSS("third-party/sweetalert2")
]).then(() => new App({
    target: document.body
}))
