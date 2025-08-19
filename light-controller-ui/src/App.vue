<script setup lang="ts">
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { LightColor, type Intersection, type Road } from './types'
const activeLight = ref<string>('red')
const intersections = ref<Intersection[]>([])

const handleActiveLightChange = () => {
  axios.post('http://localhost:8080/intersections', { activeLight: activeLight.value })
    .then(console.log)
    .catch(console.error)
}

function addIntersection() {
  axios.post('http://localhost:8080/intersections/create')
    .then(response => {
      intersections.value.push(response.data);
    })
    .catch(console.error)
}

function setIntersectionActive(intersection:Intersection) {
  axios.post('http://localhost:8080/intersections/setActive', {isActive: intersection.active, intersectionId: intersection.intersectionId })
    .then(console.log)
    .catch(console.error)
}

function setColorSeconds(event:Event, intersection:Intersection, road:Road, lightColor:LightColor) {
  let newSeconds = event.target != null ? Number( (event.target as HTMLInputElement)?.value ) : 0
  if (event.target instanceof EventTarget) {
    road.lights.forEach( (light) => {
      if (lightColor === LightColor.GREEN)
        light.lightConfiguration.greenSeconds = newSeconds
      else if (lightColor === LightColor.YELLOW)
        light.lightConfiguration.yellowSeconds = newSeconds
    })
  }
  axios.post("http://localhost:8080/intersections/setSecondsForRoadAndColor", 
    {intersectionId: intersection.intersectionId, roadId: road.roadId, lightColor: lightColor, seconds: newSeconds})
    .then(console.log)
    .catch(console.error)
}

onMounted(() => {
  const intersectionPollingInterval = setInterval(() => {
    axios.get('http://localhost:8080/intersections')
      .then(response => {
        intersections.value.length = 0
        intersections.value.push(...response.data)
      })
      .catch(console.error)
  }, 1000);
})

</script>

<template>
  <header>
    <div class="wrapper">
      <h1>Intersection Light Controller</h1>
    </div>
  </header>

  <main>
    <div>
      <button @click="addIntersection" >Add Intersection</button>
    </div>
    <div v-for="intersection in intersections" style="border: 1px black solid;">
      <label>
        <input type="checkbox" v-model="intersection.active" @change="setIntersectionActive(intersection)">
        Active
      </label>
      <div v-for="road in intersection.roads">
        <span v-for="light in road.lights">
          {{ light.active ? light.lightColor + " " : "Off " }}
        </span>
        <span>
          <label>
            <input type="text" size="2" v-model="road.greenSeconds" @change="setColorSeconds($event, intersection, road, LightColor.GREEN)" >
            Green Seconds
          </label>
          <label>
            <input type="text" size="2" v-model="road.yellowSeconds" @change="setColorSeconds($event, intersection, road, LightColor.YELLOW)" >
            Yellow Seconds
          </label>
        </span>
      </div>
    </div>
    <div class="light-controller">
      <div class="light">
        <label>
          <input type="radio" value="red" class="red" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Red
        </label>
        <label>
          <input type="radio" value="yellow" class="yellow" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Yellow
        </label>
        <label>
          <input type="radio" value="green" class="green" v-model="activeLight" name="light"
            @change="handleActiveLightChange" /> Green
        </label>
      </div>

      <p>Active light: {{ activeLight }}</p>
    </div>
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    margin: calc(var(--section-gap) / 4);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.light-controller {
  display: grid;
  place-items: center;
  gap: 1rem;

  .light {
    display: grid;
    gap: .5rem;
  }

}

input[type='radio'].red {
  accent-color: #cc3232;
}

input[type='radio'].yellow {
  accent-color: #e7b416;
}

input[type='radio'].green {
  accent-color: #2dc937;
}
</style>
