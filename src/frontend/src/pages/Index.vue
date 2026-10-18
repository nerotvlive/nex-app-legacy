<script setup lang="ts">
if(localStorage.getItem("nexapp.legacy.startPage")) {
  window.location.href = "/" + localStorage.getItem("nexapp.legacy.startPage")
} else {
  window.location.href = "/dashboard"
}
</script>

<template>

</template>

<style scoped>

</style>