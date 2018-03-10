class Checkin < ApplicationRecord
	belongs_to :room
  	belongs_to :student, optional: true
  	belongs_to :mentor, optional: true

  	validates :room_id, presence: true

  	def self.roster room
  		Checkin.where(room_id: room.id)
  	end
end
