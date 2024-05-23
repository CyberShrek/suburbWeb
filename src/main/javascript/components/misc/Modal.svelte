<script>

    import {createEventDispatcher, onDestroy, onMount} from "svelte"

    const dispatch = createEventDispatcher()

    let rootElement

    // Close on escape key
    window.addEventListener("keydown", (e) => {
        if (e.key === "Escape") {
            exit()
        }
    })

    onMount(() => {
        document.body.appendChild(rootElement)
        document.body.style.overflow = "hidden"
    })
    onDestroy(() => {
        document.body.style.overflow = ""
    })

    function exit(){
        dispatch("exit")
    }

</script>

<div bind:this={rootElement}
     class="modal-backdrop"
     role="none"
     tabindex="-1"
     on:click={exit}>

    <div class="modal"
         on:click={e => e.stopPropagation()}
         role="none">
        <slot/>
    </div>
</div>