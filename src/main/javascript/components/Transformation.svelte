<script>
    import Loader from "./misc/Loader.svelte"
    import Table from "./tables/Table.svelte"
    import {retrieveMainTable} from "../api/tables.js"

    retrieveMainTable
</script>

{#await retrieveMainTable()}
    <Loader/>
{:then mainTable}
    <Table jsonArray={mainTable}/>
{/await}