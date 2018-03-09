class Room < ApplicationRecord
	has_many :checkins
  	has_many :students, through: :checkins
  	has_many :mentors, through: :checkins

	validates :name, presence: true
	validates :date, presence: true
	validates :time, presence: true

	require 'csv'

	def self.import(file)
	  CSV.foreach(file.path, headers:true) do |row|
	  	hash = row.to_hash
	  	hash['date'] = Date.strptime(hash['date'], '%m/%d/%Y')
	  	hash['time'] = hash['time'].to_time(:utc)
	  	Room.create! hash
	  end
	end
end
