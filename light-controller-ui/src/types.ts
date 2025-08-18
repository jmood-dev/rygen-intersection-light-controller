export interface Intersection {
  id: string;
  active: boolean;
  roads: Road[];
}

export interface Road {
  id: string;
  lights: Light[];
}

export interface Light {
  id: string;
  active: boolean;
  lightColor: LightColor;
  lightConfiguration: LightConfiguration;
  secondsOnColor: number;
}

export interface LightConfiguration {
  id: string;
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