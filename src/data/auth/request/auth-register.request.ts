export type AuthRegisterREQ = {
  fullname: string;
  email: string;
  nationality: string;
  username: string;
  password: string;
  recommenderId: string | null;
  distributorId: string | null;
};
