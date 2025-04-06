export interface FeedbackDTO {
  id: number;
  name: string;
  driverName: string;
  feedbackDate: string;
  feedbackContent: string;
  tripId: number;
  status: string;
  tripName?: string;
}
