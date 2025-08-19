export interface Intersection {
  intersectionId: number;
  active: boolean;
  roads: Road[];
}

export interface Road {
  roadId: number;
  lights: Light[];
  greenSeconds: number;
  yellowSeconds: number;
}

export interface Light {
  lightId: number;
  active: boolean;
  lightColor: LightColor;
  lightConfiguration: LightConfiguration;
  secondsOnColor: number;
}

export interface LightConfiguration {
  lightConfigurationId: number;
  redSeconds: number;
  yellowSeconds: number;
  greenSeconds: number;
  isSynchronized: boolean;
}

export enum LightColor {
  RED = "RED",
  YELLOW = "YELLOW",
  GREEN = "GREEN"
}