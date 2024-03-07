import { Type } from "./type";

export class UserData {
  userId?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  jobTitle?: Type;
  imageURL?: string;
  creationDate?: Date;
  lastUpdateDate?: Date;
}
