export const LocationTypeLocation = 'Location';
export const LocationTypeOther = 'Other';
export const LocationTypes = [LocationTypeLocation, LocationTypeOther] as const;
export type LocationType = (typeof LocationTypes)[number];
