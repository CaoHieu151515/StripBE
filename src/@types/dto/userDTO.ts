export interface UserGetAllDTO {
  userId: number;
  username: string;
  firstName: string | null;
  lastName: string | null;
  email: string;
  gender: string | null;
  phoneNumber: string | null;
  active: boolean;
  roles: string[];
}
