<script>

    export let jsonArray = []

</script>

{#if jsonArray?.length > 0}
    <table>
        <thead>
            <tr>
                {#each Object.keys(jsonArray[0]) as cell}
                    <th>{cell}</th>
                {/each}
            </tr>
        </thead>
        <tbody>
            {#each jsonArray as json}
                <tr>
                    {#each Object.values(json) as cell}
                        <td>{cell}</td>
                    {/each}
                </tr>
            {/each}
        </tbody>
    </table>
{/if}