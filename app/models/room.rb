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
	  	dt = hash['date'].strftime('%d/%m/%Y') + ' ' + hash['time']
	  	hash['time'] = dt.to_time(:utc)
	  	Room.create! hash
	  end
	end

	def self.upcoming?
		# hacky because Time.current doesn't use correct timezone in the where clause for some reason
		Room.where("date > ? OR date = ? AND time > ?", Date.today, Date.today, Time.current-7*60*60)
	end
end
