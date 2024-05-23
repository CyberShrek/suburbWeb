import resolveNodeJs from '@rollup/plugin-node-resolve'
import applyTerser from '@rollup/plugin-terser'
import clean from "@rollup-extras/plugin-clean"
import sveltePreprocess from "svelte-preprocess"
import svelte from "rollup-plugin-svelte"

export default  {

    dev: false,
    input: [`./src/main/javascript/transformation.js`],
    output: [
        {
            dir: "./src/main/resources/static/js/built",
            format: "es",
            sourcemap: true
        }
    ],
    plugins: [
        clean(),
        resolveNodeJs({
            mainFields: [ "module", "browser", "main" ],
            dedupe: ['s']
        }),
        applyTerser(),
        svelte({
            preprocess: sveltePreprocess(),
        })
    ]
}