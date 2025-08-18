<script setup lang="ts">
import { ref } from 'vue'
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
  //TODO: refactor to have the backend generate the intersection, then put the result in intersections
  intersections.value.push({
    id: crypto.randomUUID(),
    active: false,
    roads: [{
      id: crypto.randomUUID(),
      lights: [{
        id: crypto.randomUUID(),
        active: false,
        lightColor: LightColor.RED,
        lightConfiguration: {
          id: crypto.randomUUID(),
          redSeconds: 5,
          yellowSeconds: 2,
          greenSeconds: 3,
          isSynchronized: true
        },
        secondsOnColor: 0
      }, {
        id: crypto.randomUUID(),
        active: false,
        lightColor: LightColor.RED,
        lightConfiguration: {
          id: crypto.randomUUID(),
          redSeconds: 5,
          yellowSeconds: 2,
          greenSeconds: 3,
          isSynchronized: true
        },
        secondsOnColor: 0
      }]
    }, {
      id: crypto.randomUUID(),
      lights: [{
        id: crypto.randomUUID(),
        active: false,
        lightColor: LightColor.RED,
        lightConfiguration: {
          id: crypto.randomUUID(),
          redSeconds: 5,
          yellowSeconds: 2,
          greenSeconds: 3,
          isSynchronized: true
        },
        secondsOnColor: 0
      }, {
        id: crypto.randomUUID(),
        active: false,
        lightColor: LightColor.RED,
        lightConfiguration: {
          id: crypto.randomUUID(),
          redSeconds: 5,
          yellowSeconds: 2,
          greenSeconds: 3,
          isSynchronized: true
        },
        secondsOnColor: 0
      }]
    }]
  })
}

function setGreenSeconds(event:Event, road:Road) {
  let newSeconds = event.target != null ? Number( (event.target as HTMLInputElement)?.value ) : 0
  if (event.target instanceof EventTarget) {
    road.lights.forEach( (light) => {
      light.lightConfiguration.greenSeconds = newSeconds
    })
  }
}

function setYellowSeconds(event:Event, road:Road) {
  let newSeconds = event.target != null ? Number( (event.target as HTMLInputElement)?.value ) : 0
  if (event.target instanceof EventTarget) {
    road.lights.forEach( (light) => {
      light.lightConfiguration.yellowSeconds = newSeconds
    })
  }
  
}
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
        <input type="checkbox" v-model="intersection.active" >
        Active
      </label>
      <div v-for="road in intersection.roads">
        <span v-for="light in road.lights">
          {{ light.lightColor + " " }}
        </span>
        <span>
          <label>
            <input type="text" size="2" @change="setGreenSeconds($event, road)" >
            Green Seconds
          </label>
          <label>
            <input type="text" size="2" @change="setYellowSeconds($event, road)" >
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
