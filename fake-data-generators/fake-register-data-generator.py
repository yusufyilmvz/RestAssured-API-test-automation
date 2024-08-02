from faker import Faker
import json
import random
import argparse

class FakeDataGenerator:
    def __init__(self, num_records, null_probability=0.2):
        self.num_records = num_records
        self.null_probability = null_probability
        self.fake = Faker()

    def generate_record(self):
        """Generates a single record with attributes that may be null."""
        record = {
            "name": self._get_value_or_null(self.fake.first_name()),
            "lastname": self._get_value_or_null(self.fake.last_name()),
            "username": self._get_value_or_null(self.fake.email()),
            "password": self._get_value_or_null("123"),  # Static value as specified
            "gender": self._get_value_or_null(self.fake.random_element(elements=["Male", "Female"])),
            "authorities": self._get_value_or_null(["ROLE_USER"])
        }
        return record

    def _get_value_or_null(self, value):
        """Randomly returns the value or null based on the null probability."""
        return value if random.random() > self.null_probability else None

    def generate_fake_data(self):
        """Generates multiple records."""
        return [self.generate_record() for _ in range(self.num_records)]

    @staticmethod
    def save_to_json(filename, data):
        """Saves the data to a JSON file."""
        with open(filename, 'w') as file:
            json.dump(data, file, indent=2)

def parse_arguments():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(description="Generate fake data and save to a JSON file.")
    parser.add_argument('num_records', type=int, help='Number of fake records to generate')
    parser.add_argument('--null_probability', type=float, default=0.2, help='Probability of an attribute being null (default: 0.2)')
    parser.add_argument('--output', type=str, default='../src/test/resources/jsonData/registerUser.json', help='Output JSON file name (default: fake_data.json)')

    return parser.parse_args()

if __name__ == "__main__":
    args = parse_arguments()

    generator = FakeDataGenerator(num_records=args.num_records, null_probability=args.null_probability)
    fake_data = generator.generate_fake_data()
    generator.save_to_json(args.output, fake_data)
    print(f"Data has been saved to '{args.output}'")
