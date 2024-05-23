<script>
    import Loader from "./misc/Loader.svelte"
    import Table from "./tables/Table.svelte"
    import {retrieveMainTable, retrieveCostTable, retrieveAdiTable, retrieveCO22} from "../api/tables.js"
    import Modal from "./misc/Modal.svelte";

    let clickedJson

</script>

<h2>Главная таблица второго уровня (rawdl2.l2_prig_main)</h2>
<Table jsonArrayPromise={retrieveMainTable()}
       bind:clickedJson/>

{#if clickedJson != null}
    <Modal on:exit={() => console.log(clickedJson = null)}>
        <h2>Выбрано:</h2>
        <Table jsonArray={[clickedJson]}/>
        <p>Стоимости (rawdl2.l2_prig_cost):</p>
        <Table jsonArrayPromise={retrieveCostTable(clickedJson.idnum)}/>
        <p>Дополнительно (rawdl2.l2_prig_adi):</p>
        <Table jsonArrayPromise={retrieveAdiTable(clickedJson.idnum)}/>

        <h2>Ожидаемый выход ЦО22:</h2>
        {#await retrieveCO22(clickedJson.idnum)}
            <Loader/>
        {:then co22}
            {#each Object.entries(co22) as entry}
                <p>{entry[0]}</p>
                <Table jsonArray={[entry[1]]}/>
            {/each}
        {/await}
    </Modal>
{/if}