// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		type Link = {
			distance: number;
			duration: number;
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
		};

		type Passenger = {
			id: number;
			first_name: string;
			last_name: string;
		};

		type Schedule = {
			id: number;
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
			direction: App.TrainDirection;
		};

		type Station = {
			loading_time: number;
			train_route: string;
			name: string;
		};

		type Ticket = {
			departure: Date;
			passenger_id: number;
			train_id: number;
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
			direction: App.TrainDirection;
		};

		type Train = {
			id: number;
			schedule_id: number | null;
			schedule_route: string | null;
			schedule_departure: Date | null;
			station_name: string | null;
			station_arrival: Date | null;
			station_departure: Date | null;
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
