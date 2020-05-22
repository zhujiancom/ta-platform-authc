<template>
    <div id="refresh">

    </div>
</template>

<script>
    export default {
        name: 'refresh',
        beforeRouteEnter(to, from, next){
            next(vm => {
                vm.$router.replace(from.fullPath)
            })
        }
    }
</script>
