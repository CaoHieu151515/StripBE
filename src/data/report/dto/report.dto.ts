export interface ReportDTO {
  id: number;
  name: string;
  driverName: string;
  reportDate: string;
  reportContent: string;
  tripId: number;
  status: string;
  tripName?: string;
}
