// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		type Link = {
			originStation: App.Station;
			destStation: App.Station;
			distance: number;
			duration: number;
		};

		type Passenger = {
			id: number;
			first_name: string;
			last_name: string;
		};

		type Schedule = {
			id: number;
			originStation: App.Station;
			destStation: App.Station;
			direction: App.TrainDirection;
		};

		type Station = {
			name: string;
			train_route: string;
			loading_time: number;
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
			// schedule
			schedule: App.Schedule | null;
			station: App.Station | null;
			link: App.Link | null;
			schedule_departure: Date | null;
			station_arrival: Date | null;
			station_departure: Date | null;
			train_status: string;
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
