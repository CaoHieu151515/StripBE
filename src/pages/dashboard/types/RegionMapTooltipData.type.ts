export type RegionMapTooltipInfoContent = {
  countryName: string;
  value: number;
};

export type RegionMapTooltipInfoContentWithCountryCode = {
  countryCode: string;
} & RegionMapTooltipInfoContent;

export type RegionMapTooltipInfo = {
  startDate: number;
  endDate: number;
  totalValue: number;
  values: RegionMapTooltipInfoContentWithCountryCode[];
};

export type RegionMapTooltipCoord = {
  x: number;
  y: number;
};

export type RegionMapTooltipData = {
  info: RegionMapTooltipInfoContentWithCountryCode;
  coord: RegionMapTooltipCoord;
};
