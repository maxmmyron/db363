// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		type Link = {
			origin: App.Station;
			dest: App.Station;
			distance: number;
			duration: number;
		};

		type Passenger = {
			id: number;
			firstName: string;
			lastName: string;
		};

		type Schedule = {
			id: number;
			origin: App.Station;
			dest: App.Station;
			direction: App.TrainDirection;
		};

		type Station = {
			name: string;
			route: string;
			loadingTime: number;
		};

		type Ticket = {
			passenger: App.Passenger;
			train: App.Train;
			origin: App.Station;
			dest: App.Station;
			departure: Date;
			direction: App.TrainDirection;
		};

		type Train = {
			id: number;
			schedule: App.Schedule | null;
			station: App.Station | null;
			link: App.Link | null;
			schedDep: Date | null;
			stationArrival: Date | null;
			stationDep: Date | null;
			status: string;
		};

		enum TrainDirection { "INBOUND", "OUTBOUND" };

		// interface Error {}
		// interface Locals {}
		// interface PageData {}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
