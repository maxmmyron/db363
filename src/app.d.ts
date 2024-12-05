// See https://svelte.dev/docs/kit/types#app.d.ts
// for information about these interfaces
declare global {
	namespace App {
		type Link = {
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
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
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
			direction: App.TrainDirection;
		};

		type Station = {
			name: string;
			train_route: string;
			loading_time: number;
		};

		type Ticket = {
			passenger_id: number;
			train_id: number;
			departure: Date;
			origin_name: string;
			origin_route: string;
			dest_name: string;
			dest_route: string;
			direction: App.TrainDirection;
		};

		type Train = {
			id: number;
			// schedule
			schedule_id: number | null;
			schedule_departure: Date | null;
			// curr station
			station_name: string | null;
			station_route: string | null;
			station_arrival: Date | null;
			station_departure: Date | null;
			// link
			link_origin_name: string | null;
			link_origin_route: string | null;
			link_dest_name: string | null
			link_dest_route: string | null;
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
