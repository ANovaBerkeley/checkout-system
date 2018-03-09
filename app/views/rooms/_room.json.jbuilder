json.extract! room, :id, :name, :date, :time, :location, :created_at, :updated_at
json.url room_url(room, format: :json)
