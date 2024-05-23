<script>
    import Loader from "../misc/Loader.svelte"

    export let
        jsonArray = null,
        jsonArrayPromise = null,
        clickedJson = null

    if(jsonArray != null) {
        jsonArrayPromise = Promise.resolve(jsonArray)
    }

</script>

{#if jsonArrayPromise}
    {#await jsonArrayPromise}
        <Loader/>
    {:then _jsonArray}
        {#if _jsonArray != null}
            <div class="table">
                <table>
                    <thead>
                    <tr>
                        {#each Object.keys(_jsonArray[0]) as key}
                            <th>{key}</th>
                        {/each}
                    </tr>
                    </thead>
                    <tbody>
                    {#each _jsonArray as json}
                        <tr on:click={() => clickedJson = json}>
                            {#each Object.values(json) as value}
                                <td class={typeof value}>{value}</td>
                            {/each}
                        </tr>
                    {/each}
                    </tbody>
                </table>
            </div>
        {/if}
    {/await}
{/if}