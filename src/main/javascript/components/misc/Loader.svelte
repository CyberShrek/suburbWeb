<script>
    import {fade} from "svelte/transition"
</script>

<div class="loader"
     in:fade>
    <svg class="circular-loader" viewBox="25 25 50 50">
        <circle class="loader-path" cx="50" cy="50" r="20" fill="none" stroke-width="2"/>
    </svg>
</div>