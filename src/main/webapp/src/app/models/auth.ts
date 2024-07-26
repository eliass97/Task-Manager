export class RegistrationRequest {
  email?: string;
  username?: string;
  password?: string;

  constructor(email?: string, username?: string, password?: string) {
    this.email = email;
    this.username = username;
    this.password = password;
  }
}

export class LoginRequest {
  username?: string;
  password?: string;
  refreshToken?: string;

  constructor(username?: string, password?: string) {
    this.username = username;
    this.password = password;
  }
}

export class LoginResponse {
  userId?: number;
  token?: string;
  refreshToken?: string;
  expiresAt?: Date;
}
